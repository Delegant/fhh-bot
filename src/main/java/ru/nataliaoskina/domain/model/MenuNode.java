package ru.nataliaoskina.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuNode {

    private UUID id;
    private String key;
    private String label;
    private String callbackData;
    private Boolean isVisible;
    private Integer orderIndex;
    private UUID parentId;
    private Integer buttonRow;
    private Integer buttonColumn;
}
