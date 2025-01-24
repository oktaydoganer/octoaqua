package com.nexmind3.octoaqua.entity;

import com.nexmind3.octoaqua.enumeration.State;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role extends BaseObject {

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

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) @Fetch(value = FetchMode.SELECT)
	@JoinTable(name = "role_authority", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private List<Authority> authorities=new ArrayList<>();


	
	


	
	
	
	



	
	
}