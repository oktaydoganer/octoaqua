package com.nexmind3.octoaqua.entity;

import com.nexmind3.octoaqua.enumeration.State;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authority")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Authority extends BaseObject {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true)
	public long id;
	
	@Column(name="state")
	public State state;
	
	@Column(name = "name", length = 25, nullable = false, unique = true)
	public String name;

	@Column(name = "description", length = 100)
	public String description;

	
	


	
	
	
	



	
	
}