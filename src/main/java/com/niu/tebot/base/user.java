package com.niu.tebot.base;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("user")
@Accessors(chain = true)
@Data
public class user {
    public String username;
    public String password;

}
