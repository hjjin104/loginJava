package hello.login.logintest.domain.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Member {
    private Long id;
    @NotBlank
    private String userId;
    @NotBlank
    private String userPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
