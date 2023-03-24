package com.home.book.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.stereotype.Service;
import com.home.book.model.AttributeFilial;
import com.home.book.model.AttributeUser;
import com.home.book.model.DeptFilial;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * Сервис пользователя AD
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final static Set<AttributeFilial> filialList = ConcurrentHashMap.newKeySet(50);

    private final LdapTemplate ldapTemplate;

    public UserServiceImpl(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    /**
     * Получить департаменты из AD группы
     *
     * @param base имя группы AD
     * @return список департаментов
     */
    public List<AttributeFilial> getFilial(String base) {
        LdapQuery query = query()
                .searchScope(SearchScope.ONELEVEL)
                .base(base)
                .where("objectClass").is("organizationalUnit")
                .and("description").isPresent();
        return ldapTemplate.search(query, new AttributeFilialMapper());
    }

    /**
     * Mapper для LdapQuery
     */
    private class AttributeFilialMapper implements AttributesMapper<AttributeFilial> {
        public AttributeFilial mapFromAttributes(Attributes attrs) throws NamingException {
            String description = attrs.get("description") == null ? "" : attrs.get("description").get().toString();
            String telephoneNumber = attrs.get("telephoneNumber") == null ? "" : attrs.get("telephoneNumber").get().toString();
            String businessCategory = attrs.get("businessCategory") == null ? "" : attrs.get("businessCategory").get().toString();
            return new AttributeFilial(description, telephoneNumber, businessCategory);
        }
    }

    /**
     * Иницилизация списка пользователей из групп
     */
    public void initUsersList() {
        filialList.clear();
        filialList.addAll(getFilial("OU=Branch"));
        filialList.addAll(getFilial("OU=Dept"));
        List<AttributeUser> users = new ArrayList<>();
        for (DeptFilial branchFilial : DeptFilial.values()) {
            users.addAll(findAllUsersFromAD("OU=" + branchFilial + ",OU=Branch"));
        }
        users.addAll(findAllUsersFromAD("OU=Dept"));
        sortByDepartment(users);
        log.info("users with wrong department = {}", users.size());
    }

    /**
     * Заносит пользователей в департамент
     *
     * @param users список пользователей
     */
    private void sortByDepartment(List<AttributeUser> users) {
        filialList.forEach(filial -> {
            Iterator<AttributeUser> iterator = users.listIterator();
            while (iterator.hasNext()) {
                AttributeUser user = iterator.next();
                if (Objects.equals(filial.getDescription().toLowerCase(), user.getDepartment().toLowerCase())) {
                    filial.addUser(user);
                    iterator.remove();
                }
            }
        });
    }

    /**
     * Получить всех пользователей с филиалами
     *
     * @return {филиал:пользователь}
     */
    @Override
    public Set<AttributeFilial> getAllUsers() {
        return filialList;
    }

    /**
     * Получить всех пользователей из AD
     *
     * @param base Каталог
     * @return Коллекция пользователей из AD
     */
    private List<AttributeUser> findAllUsersFromAD(String base) {
        List<Map<String, String>> usersFromAD = ldapTemplate.search(base, "(objectClass=person)",
                new AttributesMapper() {
                    public Object mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        NamingEnumeration<? extends Attribute> attrsAll = attrs.getAll();
                        Map<String, String> result = new HashMap<>();
                        while (attrsAll.hasMore()) {
                            String[] next = attrsAll.next().toString().split(":");
                            result.put(next[0], next[1]);
                        }
                        return result;
                    }
                });
        return collectUsersInfo(usersFromAD);
    }

    /**
     * Собирает объекты пользователя из информации полученной от AD
     *
     * @param usersFromAD Коллекция пользователей из AD
     * @return Коллекция объектов пользователей
     */
    private List<AttributeUser> collectUsersInfo(List<Map<String, String>> usersFromAD) {
        List<AttributeUser> userList = new ArrayList<>();
        usersFromAD.forEach(map -> {
            String name = Optional.ofNullable(map.get("name")).orElse("").trim();
            if (map.get("department") != null && !name.equals("Тест Групповой Политики") && !name.equals("Учетка для тестов")) {
                String department = Optional.ofNullable(map.get("department")).orElse("").trim();
                AttributeUser attributeUser = new AttributeUser(
                        name,
                        Optional.ofNullable(map.get("title")).orElse("").trim(),
                        department,
                        Optional.ofNullable(map.get("telephoneNumber")).orElse("").trim(),
                        Optional.ofNullable(map.get("ipPhone")).orElse("").trim(),
                        Optional.ofNullable(map.get("businessCategory")).orElse("").trim());
                userList.add(attributeUser);
            }
        });
        return userList;
    }
}
