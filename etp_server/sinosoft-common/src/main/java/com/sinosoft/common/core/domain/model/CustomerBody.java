package com.sinosoft.common.core.domain.model;

import com.sinosoft.common.core.domain.entity.SysDept;

public class CustomerBody extends SysDept {
    // 管理员账号
    private String userName;
    // 管理员密码
    private String password;
    // 管理员昵称
    private String nickName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
