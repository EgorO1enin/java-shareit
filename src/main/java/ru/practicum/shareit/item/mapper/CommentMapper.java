package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "author", source = "author")
    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    CommentResponseDto toCommentResponseDto(Comment comment);
}