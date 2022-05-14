package org.yipuran.util.test;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Item
 */
@Data
public class Item{
	private String id;
	private String name;
	private LocalDateTime createAt;
	private LocalDateTime updateAt;
}
