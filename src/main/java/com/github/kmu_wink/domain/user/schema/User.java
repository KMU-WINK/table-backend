package com.github.kmu_wink.domain.user.schema;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.github.kmu_wink.common.database.BaseSchema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseSchema {

    String socialId;

    Club club;

    String name;

    String email;

    public enum Club {
        WINK, KOSS, AIM, KPSC, KOBOT, D_ALPHA, DO_UM, FOSCAR;

        @Override
        public String toString() {

           return  switch (this) {
				case WINK -> "WINK";
				case KOSS -> "KOCSS";
				case AIM -> "AIM";
				case KPSC -> "KPSC";
				case KOBOT -> "KOBOT";
				case D_ALPHA -> "D-Alpha";
				case DO_UM -> "Do-Um";
				case FOSCAR -> "Foscar";
			};
        }
    }
}

