package com.github.jaraya81.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;

@Builder
@Data
public class User {

    @Tolerate
    public User() {
        super();
    }

    private Long idUser;
    private String username;
    private String usernameSN;

    private LocalDateTime datecreation;
    private LocalDateTime dateupdate;

}
