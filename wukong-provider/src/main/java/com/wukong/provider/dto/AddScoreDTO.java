package com.wukong.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddScoreDTO implements Serializable {

    private Long userId;

    private Integer score;
}
