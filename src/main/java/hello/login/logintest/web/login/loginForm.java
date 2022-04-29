package hello.login.logintest.web.login;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class loginForm {
    @NotBlank
    private String loginId;
    @NotBlank
    private String loginPassword;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}
