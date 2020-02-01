package br.com.alura.forum.validation;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.alura.forum.dto.input.NewTopicInputDto;
import br.com.alura.forum.model.PossibleSpam;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.service.TopicService;

public class NewTopicCustomValidator implements Validator {
	
	private final TopicService topicService;
	private User loggedUser;
	
	public NewTopicCustomValidator(TopicService topicService, User loggedUser) {
		this.topicService = topicService;
		this.loggedUser = loggedUser;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return NewTopicInputDto.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
		List<Topic> topics = topicService
				.findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(loggedUser, oneHourAgo);
		
		PossibleSpam possibleSpam = new PossibleSpam(topics);
		
		if (possibleSpam.hasTopicLimitExceeded()) {
			
			long minutesToNextTopic = possibleSpam.minutesToNextTopic(oneHourAgo);
			errors.reject("newTopicInputDto.limit.exceeded",
					new Object[] {minutesToNextTopic},
			"o limite individual de novos tópicos por hora foi excedido");
			}
		
	 }

}
