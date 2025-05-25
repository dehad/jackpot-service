package com.example.jackpot.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@RequiredArgsConstructor
@Data
public class ErrorDto {

   final List<String> errors;
}
