package com.example.quizservice.service;

import com.example.quizservice.dto.QuizCollectionDTO;
import com.example.quizservice.model.QuizCollection;
import com.example.quizservice.repository.QuizCollectionRepository;
import com.example.quizservice.repository.QuizRepository;
import com.example.quizservice.testutils.TestDataFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizCollectionServiceTest {

    @Mock
    private QuizCollectionRepository quizCollectionRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private QuizCollectionService quizCollectionService;

    private QuizCollection mockCollection;
    private QuizCollectionDTO mockCollectionDTO;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() throws IOException {
        mockCollection = TestDataFactory.createMockQuizCollection(1L);
        mockCollectionDTO = TestDataFactory.createQuizCollectionDTOWithImage();
        mockFile = TestDataFactory.createMockMultipartFile();
    }

    @Test
    @DisplayName("Kiểm tra tạo QuizCollection với file ảnh thành công")
    void createQuizCollection_WithImage_Success() throws IOException {
        System.out.println("===== BẮT ĐẦU TEST: createQuizCollection_WithImage_Success =====");
        
        // Arrange
        String expectedPhotoPath = "collection/abc123_cover.jpg";
        QuizCollection savedCollection = new QuizCollection();
        savedCollection.setId(1L);
        savedCollection.setAuthorId(mockCollectionDTO.getAuthorId());
        savedCollection.setDescription(mockCollectionDTO.getDescription());
        savedCollection.setCategory(mockCollectionDTO.getCategory());
        savedCollection.setVisibleTo(mockCollectionDTO.getVisibleTo());
        savedCollection.setCoverPhoto(expectedPhotoPath);
        savedCollection.setTimestamp(LocalDateTime.now());

        when(fileStorageService.storeCollectionCoverPhoto(mockFile))
                .thenReturn(expectedPhotoPath);
        when(quizCollectionRepository.save(any(QuizCollection.class)))
                .thenReturn(savedCollection);

        System.out.println("Chuẩn bị dữ liệu test: DTO với file ảnh");
        System.out.println("Expected photo path: " + expectedPhotoPath);

        System.out.println("Đang gọi quizCollectionService.createQuizCollection");
        QuizCollection result = quizCollectionService.createQuizCollection(mockCollectionDTO, mockFile);

        System.out.println("Kiểm tra kết quả trả về");
        assertNotNull(result, "Kết quả không được null");
        assertEquals(1L, result.getId(), "ID phải là 1");
        assertEquals(mockCollectionDTO.getAuthorId(), result.getAuthorId(), "AuthorId phải khớp");
        assertEquals(mockCollectionDTO.getDescription(), result.getDescription(), "Description phải khớp");
        assertEquals(mockCollectionDTO.getCategory(), result.getCategory(), "Category phải khớp");
        assertEquals(mockCollectionDTO.getVisibleTo(), result.getVisibleTo(), "VisibleTo phải khớp");
        assertEquals(expectedPhotoPath, result.getCoverPhoto(), "Cover photo path phải khớp");
        assertNotNull(result.getTimestamp(), "Timestamp phải được set");

        verify(fileStorageService).storeCollectionCoverPhoto(mockFile);
        verify(quizCollectionRepository).save(any(QuizCollection.class));

        System.out.println("File ảnh đã được lưu với đường dẫn: " + expectedPhotoPath);
        System.out.println("===== KẾT THÚC TEST: createQuizCollection_WithImage_Success =====");
    }

    @Test
    @DisplayName("Kiểm tra tạo QuizCollection thất bại do lỗi file storage")
    void createQuizCollection_FileStorageError() throws IOException {
        System.out.println("===== BẮT ĐẦU TEST: createQuizCollection_FileStorageError =====");
        
        String errorMessage = "Lỗi khi lưu file ảnh";
        when(fileStorageService.storeCollectionCoverPhoto(mockFile))
                .thenThrow(new IOException(errorMessage));

        System.out.println("Chuẩn bị mock lỗi file storage: " + errorMessage);

        System.out.println("Đang gọi quizCollectionService.createQuizCollection với lỗi file storage");
        IOException exception = assertThrows(IOException.class, () -> {
            quizCollectionService.createQuizCollection(mockCollectionDTO, mockFile);
        });

        System.out.println("Kiểm tra exception được throw");
        assertEquals(errorMessage, exception.getMessage(), "Exception message phải khớp");

        verify(fileStorageService).storeCollectionCoverPhoto(mockFile);
        verify(quizCollectionRepository, never()).save(any(QuizCollection.class));

        System.out.println("Repository save không được gọi khi có lỗi file storage");
        System.out.println("===== KẾT THÚC TEST: createQuizCollection_FileStorageError =====");
    }

    @Test
    @DisplayName("Kiểm tra cập nhật QuizCollection với file ảnh thành công")
    void updateQuizCollection_WithImage_Success() throws IOException {
        System.out.println("===== BẮT ĐẦU TEST: updateQuizCollection_WithImage_Success =====");
        
        Long collectionId = 1L;
        String oldPhotoPath = "collection/old_cover.jpg";
        String newPhotoPath = "collection/new_cover.jpg";
        
        QuizCollection existingCollection = new QuizCollection();
        existingCollection.setId(collectionId);
        existingCollection.setAuthorId(1L);
        existingCollection.setDescription("Old description");
        existingCollection.setCategory("Old category");
        existingCollection.setVisibleTo(true);
        existingCollection.setCoverPhoto(oldPhotoPath);
        existingCollection.setTimestamp(LocalDateTime.now().minusDays(1));

        QuizCollectionDTO updateDTO = QuizCollectionDTO.builder()
                .description("Updated description")
                .category("Updated category")
                .visibleTo(false)
                .build();

        QuizCollection updatedCollection = new QuizCollection();
        updatedCollection.setId(collectionId);
        updatedCollection.setAuthorId(1L);
        updatedCollection.setDescription(updateDTO.getDescription());
        updatedCollection.setCategory(updateDTO.getCategory());
        updatedCollection.setVisibleTo(updateDTO.getVisibleTo());
        updatedCollection.setCoverPhoto(newPhotoPath);
        updatedCollection.setTimestamp(LocalDateTime.now());

        when(quizCollectionRepository.findById(collectionId))
                .thenReturn(Optional.of(existingCollection));
        when(fileStorageService.storeCollectionCoverPhoto(mockFile))
                .thenReturn(newPhotoPath);
        when(quizCollectionRepository.save(any(QuizCollection.class)))
                .thenReturn(updatedCollection);

        System.out.println("Chuẩn bị dữ liệu test: Collection ID=" + collectionId);
        System.out.println("Old photo path: " + oldPhotoPath);
        System.out.println("New photo path: " + newPhotoPath);

        System.out.println("Đang gọi quizCollectionService.updateQuizCollection");
        QuizCollection result = quizCollectionService.updateQuizCollection(collectionId, updateDTO, mockFile);

        System.out.println("Kiểm tra kết quả trả về");
        assertNotNull(result, "Kết quả không được null");
        assertEquals(collectionId, result.getId(), "ID phải khớp");
        assertEquals(updateDTO.getDescription(), result.getDescription(), "Description phải được cập nhật");
        assertEquals(updateDTO.getCategory(), result.getCategory(), "Category phải được cập nhật");
        assertEquals(updateDTO.getVisibleTo(), result.getVisibleTo(), "VisibleTo phải được cập nhật");
        assertEquals(newPhotoPath, result.getCoverPhoto(), "Cover photo path phải được cập nhật");
        assertNotNull(result.getTimestamp(), "Timestamp phải được cập nhật");

        verify(quizCollectionRepository).findById(collectionId);
        verify(fileStorageService).deleteFile(oldPhotoPath);
        verify(fileStorageService).storeCollectionCoverPhoto(mockFile);
        verify(quizCollectionRepository).save(any(QuizCollection.class));

        System.out.println("File ảnh cũ đã được xóa: " + oldPhotoPath);
        System.out.println("File ảnh mới đã được lưu: " + newPhotoPath);
        System.out.println("===== KẾT THÚC TEST: updateQuizCollection_WithImage_Success =====");
    }

    @Test
    @DisplayName("Kiểm tra cập nhật QuizCollection thất bại do lỗi file storage")
    void updateQuizCollection_FileStorageError() throws IOException {
        System.out.println("===== BẮT ĐẦU TEST: updateQuizCollection_FileStorageError =====");
        
        Long collectionId = 1L;
        String oldPhotoPath = "collection/old_cover.jpg";
        String errorMessage = "Lỗi khi lưu file ảnh mới";
        
        QuizCollection existingCollection = new QuizCollection();
        existingCollection.setId(collectionId);
        existingCollection.setAuthorId(1L);
        existingCollection.setDescription("Old description");
        existingCollection.setCategory("Old category");
        existingCollection.setVisibleTo(true);
        existingCollection.setCoverPhoto(oldPhotoPath);

        QuizCollectionDTO updateDTO = QuizCollectionDTO.builder()
                .description("Updated description")
                .category("Updated category")
                .visibleTo(false)
                .build();

        when(quizCollectionRepository.findById(collectionId))
                .thenReturn(Optional.of(existingCollection));
        when(fileStorageService.storeCollectionCoverPhoto(mockFile))
                .thenThrow(new IOException(errorMessage));

        System.out.println("Chuẩn bị mock lỗi file storage: " + errorMessage);
        System.out.println("Collection ID: " + collectionId);

        System.out.println("Đang gọi quizCollectionService.updateQuizCollection với lỗi file storage");
        IOException exception = assertThrows(IOException.class, () -> {
            quizCollectionService.updateQuizCollection(collectionId, updateDTO, mockFile);
        });

        System.out.println("Kiểm tra exception được throw");
        assertEquals(errorMessage, exception.getMessage(), "Exception message phải khớp");

        verify(quizCollectionRepository).findById(collectionId);
        verify(fileStorageService).deleteFile(oldPhotoPath);
        verify(fileStorageService).storeCollectionCoverPhoto(mockFile);
        verify(quizCollectionRepository, never()).save(any(QuizCollection.class));

        System.out.println("File cũ đã được xóa nhưng repository save không được gọi khi có lỗi");
        System.out.println("===== KẾT THÚC TEST: updateQuizCollection_FileStorageError =====");
    }


}