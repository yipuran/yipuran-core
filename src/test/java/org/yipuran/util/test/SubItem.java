package org.yipuran.util.test;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SubItem extends Item{
	private String username;
	private Integer length;
}
