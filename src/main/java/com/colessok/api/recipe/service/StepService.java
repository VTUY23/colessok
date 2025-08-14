package com.colessok.api.recipe.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.colessok.api.common.exception.AppException;
import com.colessok.api.file.entity.File;
import com.colessok.api.file.repository.FileMgmtRepository;
import com.colessok.api.file.service.FileService;
import com.colessok.api.recipe.dto.request.StepCreationRequest;
import com.colessok.api.recipe.dto.request.StepUpdateRequest;
import com.colessok.api.recipe.entity.Recipe;
import com.colessok.api.recipe.entity.RecipeStep;
import com.colessok.api.recipe.mapper.RecipeStepMapper;
import com.colessok.api.recipe.repository.RecipeRepository;
import com.colessok.api.recipe.repository.StepRepository;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StepService {
    StepRepository stepRepository;
    RecipeStepMapper recipeStepMapper;
    FileService fileService;
    FileMgmtRepository fileMgmtRepository;
    RecipeRepository recipeRepository;

    @Transactional
    public RecipeStep createStep(StepCreationRequest request) throws IOException {

        var step = recipeStepMapper.toRecipeStep(request);

        // Gán Recipe cho Step
        if (request.getRecipeId() != null) {
            Recipe recipe = recipeRepository.findById(request.getRecipeId()).orElseThrow(() -> new AppException(null));
            step.setRecipe(recipe);
        }

        MultipartFile mediaFile = request.getMedia();
        if (mediaFile != null && !mediaFile.isEmpty()) {
            var media = fileService.uploadAndSave(mediaFile);
            step.setMedia(media);
        }

        step = stepRepository.save(step);

        return step;
    }

    @Transactional
    public RecipeStep update(StepUpdateRequest request) throws IOException {

        // Gán Recipe cho Step
        var step = stepRepository.findById(request.getId()).orElseThrow(() -> new AppException(null));

        File oldMedia = step.getMedia();
        MultipartFile mediaFile = request.getMedia();
        if (mediaFile != null && !mediaFile.isEmpty()) {
            // // Xóa avatar cũ nếu có
            if (oldMedia != null) fileMgmtRepository.delete(oldMedia);
            var media = fileService.uploadAndSave(mediaFile);
            step.setMedia(media);
        } else if (mediaFile == null && oldMedia != null) {
            // Xóa avatar nếu không gửi file mới
            fileMgmtRepository.delete(oldMedia);
            step.setMedia(null);
        }

        recipeStepMapper.update(step, request);
        step = stepRepository.save(step);

        return step;
    }
}
