package com.github.kmu_wink.domain.user.constant;

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