package com.jeremias.dev.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.jeremias.dev.dtos.PostRequest;
import com.jeremias.dev.dtos.PostResponse;
import com.jeremias.dev.mappers.PostMapper;
import com.jeremias.dev.models.Post;
import com.jeremias.dev.models.Subreddit;
import com.jeremias.dev.models.User;
import com.jeremias.dev.repository.PostRepository;
import com.jeremias.dev.repository.SubredditRepository;
import com.jeremias.dev.repository.UserRepository;
import com.jeremias.dev.security.AuthService;
import com.jeremias.dev.service.PostService;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PostServiceTest {
	@Mock
	private PostRepository postRepository ;
	@Mock
	private SubredditRepository subredditRepository ;
	@Mock
	private UserRepository userRepository ;
	@Mock
	private AuthService authService ;
	@Mock
	private PostMapper postMapper;
	private PostService postService;
	@Captor
    private ArgumentCaptor<Post> postArgumentCaptor;
	@BeforeEach
    public void setup() {
        postService = new PostService(postRepository, subredditRepository, userRepository, authService, postMapper);
    }
    @Test
    @DisplayName("Should Retrieve Post by Id")
    public void shouldFindPostById() {
    	//Given
    	//Inicalize de postService
        //PostService postService = new PostService(postRepository, subredditRepository, userRepository, authService, postMapper);
        //We create a object post, for testing porpuse 
        Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        //we have a expect response,
        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test",
                "Test User", "Test Subredit", 0, 0, "1 Hour Ago", false, false);
 
        //When
        //When setup the method when call the function from repository give the return setup
        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));
        //This setUP mockito and then return the expected response weh setiup
        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);
        //We call the method 
        PostResponse actualPostResponse = postService.getPost(123L);
        
        //Then
        //we comapre the expect output with the firt object b
        Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        Assertions.assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());
    }
    
    @Test
    @DisplayName("Should Save Posts")
    public void shouldSavePosts() {
    	//Given
    	////Setup this is the reason why is better to used constructors is beacasue we can setUp all dependencies
       // PostService postService = new PostService(postRepository, subredditRepository, userRepository, authService, postMapper);
     //The user we save
        User currentUser = new User(123L, "test user", "secret password", "user@email.com", Instant.now(), true);
        //Sub
        Subreddit subreddit = new Subreddit(123L, "First Subreddit", "Subreddit Description", Collections.emptyList(), Instant.now(), currentUser);
        //Post
        Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        //Ouput
        PostRequest postRequest = new PostRequest(null, "First Subreddit", "First Post", "http://url.site", "Test");
        
        //When
        Mockito.when(subredditRepository.findByName("First Subreddit"))
                .thenReturn(Optional.of(subreddit));
        
        Mockito.when(postMapper.map(postRequest, subreddit, currentUser))
                .thenReturn(post);
        
        postService.save(postRequest);
        //Then
      //  Mockito.verify(postRepository, Mockito.times(1)).save(ArgumentMatchers.any(Post.class));
        Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());
        
        Assertions.assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);
        Assertions.assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");
    }
}
