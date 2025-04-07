package com.iqeq.dto.common;

import com.iqeq.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

	private Long id;
	private String firstName;
	private String lastName;
	private String workEmail;
	private String fullName;

	public static UserDto build(User user) {
		UserDto dto = new UserDto();
		dto.setId(user.getUserId());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setFullName(user.getFirstName() + " " + user.getLastName());
		dto.setWorkEmail(user.getWorkEmail());
		return dto;
	}

}