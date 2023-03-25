package com.jeremias.dev.models;

import com.jeremias.dev.enums.VoteType;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vote {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long voteId;
	    private VoteType voteType;
	    @NotNull
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "postId", referencedColumnName = "postId")
	    private Post post;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "userId", referencedColumnName = "id")
	    private User user;
}
