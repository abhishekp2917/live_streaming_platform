package org.example.pojo;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Resolution implements Serializable {
    private int width;
    private int height;
}