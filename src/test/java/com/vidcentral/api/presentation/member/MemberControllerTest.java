package com.vidcentral.api.presentation.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidcentral.api.application.member.MemberService;
import com.vidcentral.api.dto.auth.request.LoginRequest;
import com.vidcentral.api.dto.member.request.SignUpRequest;
import com.vidcentral.api.dto.member.request.UpdateMemberRequest;
import com.vidcentral.api.dto.auth.response.LoginResponse;
import com.vidcentral.api.dto.member.response.MemberInfoResponse;
import com.vidcentral.api.presentation.MemberController;
import com.vidcentral.support.AuthFixture;
import com.vidcentral.support.MemberFixture;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

	@InjectMocks
	private MemberController memberController;

	@Mock
	private MemberService memberService;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(memberController)
			.setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
			.build();
	}

	@DisplayName("[✅ SUCCESS] signUpMember: 성공적으로 회원가입을 완료했습니다.")
	@Test
	void signUpMember_void_success() throws Exception {
		// GIVEN
		SignUpRequest signUpRequest = AuthFixture.createSignUpRequest();

		doNothing().when(memberService).signUpMember(any(SignUpRequest.class));

		// WHEN
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/api/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest))
		);

		// THEN
		MvcResult mvcResult = resultActions.andExpect(status().isOk())
			.andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		assertThat(content.replace("\"", "")).isEqualTo("성공적으로 회원가입이 완료되었습니다.");
	}

	@DisplayName("[✅ SUCCESS] loginMember: 성공적으로 로그인을 완료했습니다.")
	@Test
	void loginMember_LoginResponse_success() throws Exception {
		// GIVEN
		LoginRequest loginRequest = AuthFixture.createLoginRequest();
		LoginResponse loginResponse = AuthFixture.createLoginResponse();

		doReturn(loginResponse).when(memberService).loginMember(any(), any(LoginRequest.class));

		// WHEN
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/api/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest))
		);

		// THEN
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").value(loginResponse.accessToken()))
			.andExpect(jsonPath("$.refreshToken").value(loginResponse.refreshToken()))
			.andReturn();
	}

	@DisplayName("[✅ SUCCESS] searchMemberInfo: 성공적으로 사용자 정보 조회를 완료했습니다.")
	@Test
	void searchMemberInfo_MemberInfoResponse_success() throws Exception {
		// GIVEN
		Long memberId = 1L;
		MemberInfoResponse memberInfoResponse = MemberFixture.createMemberInfoResponse();

		doReturn(memberInfoResponse).when(memberService).searchMemberInfo(anyLong());

		// WHEN
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/members/{memberId}", memberId)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// THEN
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.nickname").value(memberInfoResponse.nickname()))
			.andExpect(jsonPath("$.introduce").value(memberInfoResponse.introduce()))
			.andReturn();
	}

	@DisplayName("[✅ SUCCESS] updateMemberInfo: 성공적으로 사용자 정보 수정을 완료했습니다.")
	@Test
	void updateMemberInfo_void_success() throws Exception {
		// GIVEN
		UpdateMemberRequest updateMemberRequest = MemberFixture.createUpdateMemberRequest();
		MockMultipartFile newProfileImageURL = new MockMultipartFile(
			"profileImageURL", "image.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

		doNothing().when(memberService).updateMemberInfo(any(), any(), any(MultipartFile.class));

		// WHEN
		MockHttpServletRequestBuilder multipartRequestBilder = MockMvcRequestBuilders.multipart("/api/members/update")
			.file(newProfileImageURL)
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.param("updateMemberRequest", objectMapper.writeValueAsString(updateMemberRequest));

		multipartRequestBilder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		ResultActions resultActions = mockMvc.perform(multipartRequestBilder);

		// THEN
		MvcResult mvcResult = resultActions.andExpect(status().isOk())
			.andReturn();

		String content = mvcResult.getResponse().getContentAsString();
		assertThat(content.replace("\"", "")).isEqualTo("성공적으로 회원 정보가 업데이트되었습니다.");
	}
}
