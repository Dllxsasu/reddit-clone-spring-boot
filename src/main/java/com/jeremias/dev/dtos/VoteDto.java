package com.jeremias.dev.dtos;

import com.jeremias.dev.enums.VoteType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
	private VoteType voteType;
    private Long postId;
}
