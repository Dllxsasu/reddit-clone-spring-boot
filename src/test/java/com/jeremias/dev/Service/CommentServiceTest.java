package com.jeremias.dev.Service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jeremias.dev.exception.SpringRedditException;
import com.jeremias.dev.service.CommentService;




public class CommentServiceTest {
	@Test
    @DisplayName("Test Should Pass When Comment do not Contains Swear Words")
    public void shouldNotContainSwearWordsInsideComment() {
        CommentService commentService = new CommentService(null, null, null, null, null, null, null);
     /*   SpringRedditException exception = Assertions.assertThrows(SpringRedditException.class, () -> {
            commentService.containsSwearWords("This is shitty comment");
        });*/
        assertThatThrownBy(() ->{
        	commentService.containsSwearWords("this is a shitty comment");
        }).isInstanceOf(SpringRedditException.class)
        .hasMessage("Su comentario contiene un lenguaje que no esta permitido.");
  //      Assertions.assertFalse(false);
   //     Assertions.assertTrue(exception.getMessage().contains("Su comentario contiene un lenguaje que no esta permitido"));
    }
}
