/*
 * Copyright 2007-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aajb.domain.school;

import lombok.*;

import javax.persistence.*;
import java.util.Set;


/**
 * Annotation configured person professional bean.
 * 
 * @author David Winterfeldt
 */
@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@PrimaryKeyJoinColumn(name="ID")
public class Parent extends Person {

    private String tel;
    private String telGsm;
    private String telPro;
    private String ssn;
    private int caf;
    private String job;

    @Column(unique=true,nullable = false)
    private String email;

    @OneToMany(mappedBy = "firstParent",fetch = FetchType.LAZY)
    private Set<Registration> registrations;
    
}
