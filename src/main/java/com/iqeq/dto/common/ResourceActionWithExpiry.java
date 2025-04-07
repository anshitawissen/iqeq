package com.iqeq.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ResourceActionWithExpiry {
	private Set<ResourceAction> resourceActions;
	private Date expiry;
}