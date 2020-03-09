package com.wukong.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddScoreDTO implements Serializable {
    private static final long serialVersionUID = -4640517863417723452L;
    private String username;

    private int scoreToAdd;
}
