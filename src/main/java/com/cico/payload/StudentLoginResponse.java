package com.cico.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginResponse {

private	String token;
private	Boolean isDeviceIdDifferent;
private	Boolean isDeviceAlreadyInUse;
private	Boolean isFeesDue;

	
}

