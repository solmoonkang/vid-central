package com.vidcentral.api.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vidcentral.api.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findMemberByEmail(String email);

	boolean existsMemberByEmail(String email);

	boolean existsMemberByNickname(String nickname);

	boolean existsMemberByResidentNumber(String residentNumber);
}
