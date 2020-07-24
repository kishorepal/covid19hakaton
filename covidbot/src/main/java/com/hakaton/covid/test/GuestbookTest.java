package com.hakaton.covid.bot;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
public class GuestbookTest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String message;
	
	private String imageUri;
}

