package hanu.example.corespringsecurity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name="TEST")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEST_ID")
    private Long id;

    @Column(name="NAME")
    private String name;

    public Test(String name) {
        this.name = name;
    }
}
