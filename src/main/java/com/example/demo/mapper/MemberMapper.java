package com.example.demo.mapper;

import com.example.demo.dto.MemberInput;
import com.example.demo.dto.MemberResponse;
import com.example.demo.model.Member;

public class MemberMapper {

    public static Member toEntity(MemberInput input) {
        Member member = new Member();
        member.setName(input.name());
        member.setEmail(input.email());
        member.setPhone(input.phone());
        return member;
    }

    public static void updateEntity(Member member, MemberInput input) {
        member.setName(input.name());
        member.setEmail(input.email());
        member.setPhone(input.phone());
    }

    public static MemberResponse toResponse(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getPhone()
        );
    }
}
