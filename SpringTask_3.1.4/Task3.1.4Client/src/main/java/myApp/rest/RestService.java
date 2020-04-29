package myApp.rest;

import myApp.model.Role;
import myApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class RestService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email.isEmpty() || email == null) {
            throw new UsernameNotFoundException("Username is empty");
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(email, headers);
            ResponseEntity<User> responseEntity = restTemplate.exchange("http://localhost:8081/admin/email",
                    HttpMethod.POST, entity, User.class);
            return responseEntity.getBody();
        }
    }

    public User getUserByEmail(String email) {
        if (email != null && !email.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            HttpEntity<String> userHttpEntity = new HttpEntity<>(email, headers);

            ResponseEntity<User> responseEntity = restTemplate
                    .exchange("http://localhost:8081/admin/email", HttpMethod.POST, userHttpEntity,
                            new ParameterizedTypeReference<User>() {});
            return responseEntity.getBody();
        } else {
            return null;
        }
    }

    public List<User> getAllUsers() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List<User>> response = restTemplate.exchange("http://localhost:8081/admin/list",
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<User>>() {});
        return response.getBody();
    }

    public User getUser(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Long> entity = new HttpEntity<>(id, headers);
        ResponseEntity<User> response = restTemplate.exchange("http://localhost:8081/admin/" + id, HttpMethod.GET,
                entity, new ParameterizedTypeReference<User>() {});
        return response.getBody();
    }

    public void deleteUser(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Long> entity = new HttpEntity<>(id, headers);
        restTemplate.delete("http://localhost:8081/admin/" + id);
    }

    public User updateUser(User user, String role) {

        HttpHeaders headers = new HttpHeaders();
        HashMap<String, User> param = new HashMap<>();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        Long id = user.getId();
        if (role == null) { // authorized admin, roles not changes
            param.put("user", user);
            HttpEntity<HashMap<String, User>> entity = new HttpEntity<>(param, headers);
            ResponseEntity<User> userResponseEntity = restTemplate.exchange("http://localhost:8081/admin/"+id,
                    HttpMethod.PUT, entity, User.class);
            return userResponseEntity.getBody();
        }

        if (user.getRoles().size() == 2) { // admin + user
            if (role.equals("USER")) { // delete role admin
                user.getAuthorities().clear();
                user.getRoles().clear();


                HttpEntity<Long> someEnt = new HttpEntity<>(id, headers);
                restTemplate.exchange("http://localhost:8081/admin/role/" + id, HttpMethod.DELETE,
                        someEnt, new ParameterizedTypeReference<Object>() {});

                Role newRole = new Role();
                newRole.setValue(role);
                user.getRoles().add(newRole);

                HttpEntity<User> entity = new HttpEntity<>(user, headers);
                ResponseEntity<User> userResponseEntity = restTemplate.exchange("http://localhost:8081/admin/"+id,
                        HttpMethod.PUT, entity, User.class);
                return userResponseEntity.getBody();
            } else {

                HttpEntity<User> entity = new HttpEntity<>(user, headers);
                ResponseEntity<User> userResponseEntity = restTemplate.exchange("http://localhost:8081/admin/"+id,
                        HttpMethod.PUT, entity, User.class);
                return userResponseEntity.getBody();
            }
        } else { //size == 1  - user
            if (role.equals("USER")) { // не менять роль

                HttpEntity<User> entity = new HttpEntity<>(user, headers);
                ResponseEntity<User> userResponseEntity = restTemplate.exchange("http://localhost:8081/admin/"+id,
                        HttpMethod.PUT, entity, User.class);
                return userResponseEntity.getBody();
            } else { // добавить роль admin
                Role newRole = new Role();
                newRole.setValue(role);
                user.getRoles().add(newRole);

                HttpEntity<User> entity = new HttpEntity<>(user, headers);
                ResponseEntity<User> userResponseEntity = restTemplate.exchange("http://localhost:8081/admin/"+id,
                        HttpMethod.PUT, entity, User.class);
                return userResponseEntity.getBody();
            }
        }
    }

    public User saveUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.postForObject("http://localhost:8081/admin/", entity, User.class);
    }
}
