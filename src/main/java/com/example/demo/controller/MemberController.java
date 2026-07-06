package com.example.demo.controller;

import com.example.demo.dto.MemberInput;
import com.example.demo.dto.MemberResponse;
import com.example.demo.service.MemberService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @QueryMapping
    public List<MemberResponse> allMembers() {
        return memberService.findAll();
    }

    @QueryMapping
    public MemberResponse memberById(@Argument Long id) {
        return memberService.findById(id);
    }

    @MutationMapping
    public MemberResponse createMember(@Argument MemberInput input) {
        return memberService.create(input);
    }

    @MutationMapping
    public MemberResponse updateMember(@Argument Long id, @Argument MemberInput input) {
        return memberService.update(id, input);
    }

    @MutationMapping
    public boolean deleteMember(@Argument Long id) {
        return memberService.delete(id);
    }
}
