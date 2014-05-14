package at.tuwien.sentimentanalyzer.entities;

import static javax.persistence.GenerationType.AUTO;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Just a sample Entity for testing
 * @author CLF
 *
 */
@Entity
@Table
public class Foo {
	@Id
	@GeneratedValue(strategy = AUTO)
	@Column
	private long id;
	@Column(length = 200, nullable = true)
	private String description;
	public Foo() {

	}
	public Foo(String description) {
		this.description = description;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}