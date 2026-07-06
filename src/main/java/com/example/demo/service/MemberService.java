package com.example.demo.service;

import com.example.demo.dto.MemberInput;
import com.example.demo.dto.MemberResponse;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
            .map(MemberMapper::toResponse)
            .toList();
    }

    public MemberResponse findById(Long id) {
        return memberRepository.findById(id)
            .map(MemberMapper::toResponse)
            .orElseThrow(() -> new RuntimeException("Member not found: " + id));
    }

    @Transactional
    public MemberResponse create(MemberInput input) {
        Member member = MemberMapper.toEntity(input);
        return MemberMapper.toResponse(memberRepository.save(member));
    }

    @Transactional
    public MemberResponse update(Long id, MemberInput input) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found: " + id));
        MemberMapper.updateEntity(member, input);
        return MemberMapper.toResponse(memberRepository.save(member));
    }

    @Transactional
    public boolean delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new RuntimeException("Member not found: " + id);
        }
        memberRepository.deleteById(id);
        return true;
    }
}
