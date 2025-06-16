package com.example.quizservice.service;

import com.example.quizservice.client.UserClient;
import com.example.quizservice.dto.QuizDTO;
import com.example.quizservice.model.Quiz;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuizCollectionRepository quizCollectionRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private QuizService quizService;

    private Quiz mockQuiz;
    private QuizCollection mockQuizCollection;
    private QuizDTO mockQuizDTO;

    @BeforeEach
    void setUp() {
        mockQuiz = TestDataFactory.createMockQuiz(1L);
        mockQuizCollection = createMockQuizCollection(1L);
        mockQuizDTO = createMockQuizDTO();
    }

    private QuizCollection createMockQuizCollection(Long id) {
        QuizCollection collection = new QuizCollection();
        collection.setId(id);
        collection.setCategory("Test Collection");
        collection.setDescription("Test Collection Description");
        return collection;
    }

    private QuizDTO createMockQuizDTO() {
        return QuizDTO.builder()
                .userId(1L)
                .title("Test Quiz")
                .description("Test Description")
                .keyword("test")
                .visible(true)
                .visibleQuizQuestion(true)
                .shuffle(false)
                .quizCollectionId(1L)
                .build();
    }

    @Test
    @DisplayName("Kiểm tra lấy tất cả Quiz thành công")
    void getAllQuizzes_Success() {
        System.out.println("===== BẮT ĐẦU TEST: getAllQuizzes_Success =====");
        
        List<Quiz> expectedQuizzes = Arrays.asList(
            TestDataFactory.createMockQuiz(1L),
            TestDataFactory.createMockQuiz(2L)
        );
        when(quizRepository.findAll()).thenReturn(expectedQuizzes);
        
        System.out.println("Đang gọi quizService.getAllQuizzes");
        List<Quiz> result = quizService.getAllQuizzes();
        
        System.out.println("Kiểm tra kết quả trả về");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedQuizzes, result);
        
        verify(quizRepository).findAll();
        
        System.out.println("===== KẾT THÚC TEST: getAllQuizzes_Success =====");
    }

//     @Test
//     @DisplayName("Kiểm tra lấy Quiz theo ID thành công")
//     void getQuizById_Success() {
//         System.out.println("===== BẮT ĐẦU TEST: getQuizById_Success =====");
        
//         Long id = 1L;
//         when(quizRepository.findById(id)).thenReturn(Optional.of(mockQuiz));
        
//         System.out.println("Đang gọi quizService.getQuizById");
//         Optional<Quiz> result = quizService.getQuizById(id);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertTrue(result.isPresent());
//         assertEquals(mockQuiz, result.get());
        
//         verify(quizRepository).findById(id);
        
//         System.out.println("===== KẾT THÚC TEST: getQuizById_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra lấy Quiz theo ID không tồn tại")
//     void getQuizById_NotFound() {
//         System.out.println("===== BẮT ĐẦU TEST: getQuizById_NotFound =====");
        
//         Long id = 999L;
//         when(quizRepository.findById(id)).thenReturn(Optional.empty());
        
//         System.out.println("Đang gọi quizService.getQuizById với ID không tồn tại");
//         Optional<Quiz> result = quizService.getQuizById(id);
        
//         System.out.println("Kiểm tra kết quả trả về phải là empty");
//         assertFalse(result.isPresent());
        
//         verify(quizRepository).findById(id);
        
//         System.out.println("===== KẾT THÚC TEST: getQuizById_NotFound =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra lấy Quiz mới nhất thành công")
//     void getLatestQuizzes_Success() {
//         System.out.println("===== BẮT ĐẦU TEST: getLatestQuizzes_Success =====");
        
//         List<Quiz> expectedQuizzes = Arrays.asList(
//             TestDataFactory.createMockQuiz(2L),
//             TestDataFactory.createMockQuiz(1L)
//         );
//         when(quizRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(expectedQuizzes);
        
//         System.out.println("Đang gọi quizService.getLatestQuizzes");
//         List<Quiz> result = quizService.getLatestQuizzes();
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(2, result.size());
//         assertEquals(expectedQuizzes, result);
        
//         verify(quizRepository).findTop10ByOrderByCreatedAtDesc();
        
//         System.out.println("===== KẾT THÚC TEST: getLatestQuizzes_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra lấy Quiz theo userId thành công")
//     void getQuizzesByUserId_Success() {
//         System.out.println("===== BẮT ĐẦU TEST: getQuizzesByUserId_Success =====");
        
//         Long userId = 1L;
//         List<Quiz> expectedQuizzes = Arrays.asList(
//             TestDataFactory.createMockQuiz(1L),
//             TestDataFactory.createMockQuiz(2L)
//         );
//         when(quizRepository.findByUserId(userId)).thenReturn(expectedQuizzes);
        
//         System.out.println("Đang gọi quizService.getQuizzesByUserId");
//         List<Quiz> result = quizService.getQuizzesByUserId(userId);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(2, result.size());
//         assertEquals(expectedQuizzes, result);
        
//         verify(quizRepository).findByUserId(userId);
        
//         System.out.println("===== KẾT THÚC TEST: getQuizzesByUserId_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra lấy Quiz công khai thành công")
//     void getPublicQuizzes_Success() {
//         System.out.println("===== BẮT ĐẦU TEST: getPublicQuizzes_Success =====");
        
//         List<Quiz> expectedQuizzes = Arrays.asList(
//             TestDataFactory.createMockQuiz(1L),
//             TestDataFactory.createMockQuiz(2L)
//         );
//         when(quizRepository.findByVisibleTrue()).thenReturn(expectedQuizzes);
        
//         System.out.println("Đang gọi quizService.getPublicQuizzes");
//         List<Quiz> result = quizService.getPublicQuizzes();
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(2, result.size());
//         assertEquals(expectedQuizzes, result);
        
//         verify(quizRepository).findByVisibleTrue();
        
//         System.out.println("===== KẾT THÚC TEST: getPublicQuizzes_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra tìm kiếm Quiz theo tiêu đề thành công")
//     void searchQuizzes_Success() {
//         System.out.println("===== BẮT ĐẦU TEST: searchQuizzes_Success =====");
        
//         String searchTerm = "quiz";
//         List<Quiz> expectedQuizzes = Arrays.asList(
//             TestDataFactory.createMockQuiz(1L),
//             TestDataFactory.createMockQuiz(2L)
//         );
//         when(quizRepository.findByTitleContainingIgnoreCase(searchTerm)).thenReturn(expectedQuizzes);
        
//         System.out.println("Đang gọi quizService.searchQuizzes");
//         List<Quiz> result = quizService.searchQuizzes(searchTerm);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(2, result.size());
//         assertEquals(expectedQuizzes, result);
        
//         verify(quizRepository).findByTitleContainingIgnoreCase(searchTerm);
        
//         System.out.println("===== KẾT THÚC TEST: searchQuizzes_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra tạo Quiz với file ảnh thành công")
//     void createQuiz_WithImage_Success() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: createQuiz_WithImage_Success =====");
        
//         String expectedPhotoPath = "quiz/test-photo.jpg";
//         when(mockFile.isEmpty()).thenReturn(false);
//         when(fileStorageService.storeQuizCoverPhoto(mockFile)).thenReturn(expectedPhotoPath);
//         when(quizCollectionRepository.findById(1L)).thenReturn(Optional.of(mockQuizCollection));
//         when(quizRepository.save(any(Quiz.class))).thenReturn(mockQuiz);
        
//         System.out.println("Đang gọi quizService.createQuiz");
//         Quiz result = quizService.createQuiz(mockQuizDTO, mockFile);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(mockQuiz, result);
        
//         verify(fileStorageService).storeQuizCoverPhoto(mockFile);
//         verify(quizCollectionRepository).findById(1L);
//         verify(quizRepository).save(any(Quiz.class));
        
//         System.out.println("===== KẾT THÚC TEST: createQuiz_WithImage_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra tạo Quiz không có file ảnh nhưng có coverPhoto trong DTO")
//     void createQuiz_WithoutImageFile_WithCoverPhotoInDTO_Success() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: createQuiz_WithoutImageFile_WithCoverPhotoInDTO_Success =====");
        
//         mockQuizDTO.setCoverPhoto("existing-photo.jpg");
//         when(quizCollectionRepository.findById(1L)).thenReturn(Optional.of(mockQuizCollection));
//         when(quizRepository.save(any(Quiz.class))).thenReturn(mockQuiz);
        
//         System.out.println("Đang gọi quizService.createQuiz");
//         Quiz result = quizService.createQuiz(mockQuizDTO, null);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(mockQuiz, result);
        
//         verify(fileStorageService, never()).storeQuizCoverPhoto(any());
//         verify(quizCollectionRepository).findById(1L);
//         verify(quizRepository).save(any(Quiz.class));
        
//         System.out.println("===== KẾT THÚC TEST: createQuiz_WithoutImageFile_WithCoverPhotoInDTO_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra tạo Quiz với file ảnh rỗng")
//     void createQuiz_EmptyImageFile_Success() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: createQuiz_EmptyImageFile_Success =====");
        
//         when(mockFile.isEmpty()).thenReturn(true);
//         when(quizCollectionRepository.findById(1L)).thenReturn(Optional.of(mockQuizCollection));
//         when(quizRepository.save(any(Quiz.class))).thenReturn(mockQuiz);
        
//         System.out.println("Đang gọi quizService.createQuiz với file rỗng");
//         Quiz result = quizService.createQuiz(mockQuizDTO, mockFile);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(mockQuiz, result);
        
//         verify(fileStorageService, never()).storeQuizCoverPhoto(any());
//         verify(quizCollectionRepository).findById(1L);
//         verify(quizRepository).save(any(Quiz.class));
        
//         System.out.println("===== KẾT THÚC TEST: createQuiz_EmptyImageFile_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra tạo Quiz với QuizCollection không tồn tại")
//     void createQuiz_QuizCollectionNotFound() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: createQuiz_QuizCollectionNotFound =====");
        
//         when(quizCollectionRepository.findById(1L)).thenReturn(Optional.empty());
        
//         System.out.println("Đang gọi quizService.createQuiz với QuizCollection không tồn tại");
//         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//             quizService.createQuiz(mockQuizDTO, null);
//         });
        
//         System.out.println("Kiểm tra exception message");
//         assertTrue(exception.getMessage().contains("Quiz Collection not found with id: 1"));
        
//         verify(quizCollectionRepository).findById(1L);
//         verify(quizRepository, never()).save(any());
        
//         System.out.println("===== KẾT THÚC TEST: createQuiz_QuizCollectionNotFound =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra tạo Quiz với QuizCollectionId không hợp lệ")
//     void createQuiz_InvalidQuizCollectionId() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: createQuiz_InvalidQuizCollectionId =====");
        
//         mockQuizDTO.setQuizCollectionId("invalid-id");
        
//         System.out.println("Đang gọi quizService.createQuiz với QuizCollectionId không hợp lệ");
//         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//             quizService.createQuiz(mockQuizDTO, null);
//         });
        
//         System.out.println("Kiểm tra exception message");
//         assertTrue(exception.getMessage().contains("Invalid quiz collection ID: invalid-id"));
        
//         verify(quizRepository, never()).save(any());
        
//         System.out.println("===== KẾT THÚC TEST: createQuiz_InvalidQuizCollectionId =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra cập nhật Quiz thành công")
//     void updateQuiz_Success() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: updateQuiz_Success =====");
        
//         Long id = 1L;
//         String newPhotoPath = "quiz/updated-photo.jpg";
        
//         when(quizRepository.findById(id)).thenReturn(Optional.of(mockQuiz));
//         when(quizCollectionRepository.findById(1L)).thenReturn(Optional.of(mockQuizCollection));
//         when(mockFile.isEmpty()).thenReturn(false);
//         when(fileStorageService.storeQuizCoverPhoto(mockFile)).thenReturn(newPhotoPath);
//         when(quizRepository.save(any(Quiz.class))).thenReturn(mockQuiz);
        
//         System.out.println("Đang gọi quizService.updateQuiz");
//         Quiz result = quizService.updateQuiz(id, mockQuizDTO, mockFile);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(mockQuiz, result);
        
//         verify(quizRepository).findById(id);
//         verify(quizCollectionRepository).findById(1L);
//         verify(fileStorageService).storeQuizCoverPhoto(mockFile);
//         verify(quizRepository).save(any(Quiz.class));
        
//         System.out.println("===== KẾT THÚC TEST: updateQuiz_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra cập nhật Quiz không tồn tại")
//     void updateQuiz_NotFound() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: updateQuiz_NotFound =====");
        
//         Long id = 999L;
//         when(quizRepository.findById(id)).thenReturn(Optional.empty());
        
//         System.out.println("Đang gọi quizService.updateQuiz với ID không tồn tại");
//         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//             quizService.updateQuiz(id, mockQuizDTO, mockFile);
//         });
        
//         System.out.println("Kiểm tra exception message");
//         assertTrue(exception.getMessage().contains("Quiz not found with id: " + id));
        
//         verify(quizRepository).findById(id);
//         verify(quizRepository, never()).save(any());
        
//         System.out.println("===== KẾT THÚC TEST: updateQuiz_NotFound =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra cập nhật Quiz với QuizCollection không tồn tại")
//     void updateQuiz_QuizCollectionNotFound() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: updateQuiz_QuizCollectionNotFound =====");
        
//         Long id = 1L;
//         when(quizRepository.findById(id)).thenReturn(Optional.of(mockQuiz));
//         when(quizCollectionRepository.findById(1L)).thenReturn(Optional.empty());
        
//         System.out.println("Đang gọi quizService.updateQuiz với QuizCollection không tồn tại");
//         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//             quizService.updateQuiz(id, mockQuizDTO, null);
//         });
        
//         System.out.println("Kiểm tra exception message");
//         assertTrue(exception.getMessage().contains("Quiz Collection not found with id: 1"));
        
//         verify(quizRepository).findById(id);
//         verify(quizCollectionRepository).findById(1L);
//         verify(quizRepository, never()).save(any());
        
//         System.out.println("===== KẾT THÚC TEST: updateQuiz_QuizCollectionNotFound =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra cập nhật Quiz với xóa file cũ và thêm file mới")
//     void updateQuiz_ReplaceExistingFile_Success() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: updateQuiz_ReplaceExistingFile_Success =====");
        
//         Long id = 1L;
//         String oldPhotoPath = "quiz/old-photo.jpg";
//         String newPhotoPath = "quiz/new-photo.jpg";
        
//         mockQuiz.setCoverPhoto(oldPhotoPath);
        
//         when(quizRepository.findById(id)).thenReturn(Optional.of(mockQuiz));
//         when(quizCollectionRepository.findById(1L)).thenReturn(Optional.of(mockQuizCollection));
//         when(mockFile.isEmpty()).thenReturn(false);
//         when(fileStorageService.storeQuizCoverPhoto(mockFile)).thenReturn(newPhotoPath);
//         when(quizRepository.save(any(Quiz.class))).thenReturn(mockQuiz);
        
//         System.out.println("Đang gọi quizService.updateQuiz để thay thế file");
//         Quiz result = quizService.updateQuiz(id, mockQuizDTO, mockFile);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(mockQuiz, result);
        
//         verify(fileStorageService).deleteFile(oldPhotoPath);
//         verify(fileStorageService).storeQuizCoverPhoto(mockFile);
//         verify(quizRepository).save(any(Quiz.class));
        
//         System.out.println("===== KẾT THÚC TEST: updateQuiz_ReplaceExistingFile_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra xóa Quiz thành công")
//     void deleteQuiz_Success() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: deleteQuiz_Success =====");
        
//         Long id = 1L;
//         String photoPath = "quiz/photo.jpg";
//         mockQuiz.setCoverPhoto(photoPath);
        
//         when(quizRepository.findById(id)).thenReturn(Optional.of(mockQuiz));
//         doNothing().when(fileStorageService).deleteFile(photoPath);
//         doNothing().when(quizRepository).delete(mockQuiz);
        
//         System.out.println("Đang gọi quizService.deleteQuiz");
//         assertDoesNotThrow(() -> quizService.deleteQuiz(id));
        
//         System.out.println("Kiểm tra các phương thức được gọi");
//         verify(quizRepository).findById(id);
//         verify(fileStorageService).deleteFile(photoPath);
//         verify(quizRepository).delete(mockQuiz);
        
//         System.out.println("===== KẾT THÚC TEST: deleteQuiz_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra xóa Quiz không có file ảnh")
//     void deleteQuiz_NoImage_Success() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: deleteQuiz_NoImage_Success =====");
        
//         Long id = 1L;
//         mockQuiz.setCoverPhoto(null);
        
//         when(quizRepository.findById(id)).thenReturn(Optional.of(mockQuiz));
//         doNothing().when(quizRepository).delete(mockQuiz);
        
//         System.out.println("Đang gọi quizService.deleteQuiz");
//         assertDoesNotThrow(() -> quizService.deleteQuiz(id));
        
//         System.out.println("Kiểm tra các phương thức được gọi");
//         verify(quizRepository).findById(id);
//         verify(fileStorageService, never()).deleteFile(any());
//         verify(quizRepository).delete(mockQuiz);
        
//         System.out.println("===== KẾT THÚC TEST: deleteQuiz_NoImage_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra xóa Quiz không tồn tại")
//     void deleteQuiz_NotFound() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: deleteQuiz_NotFound =====");
        
//         Long id = 999L;
//         when(quizRepository.findById(id)).thenReturn(Optional.empty());
        
//         System.out.println("Đang gọi quizService.deleteQuiz với ID không tồn tại");
//         RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//             quizService.deleteQuiz(id);
//         });
        
//         System.out.println("Kiểm tra exception message");
//         assertTrue(exception.getMessage().contains("Quiz not found with id: " + id));
        
//         verify(quizRepository).findById(id);
//         verify(fileStorageService, never()).deleteFile(any());
//         verify(quizRepository, never()).delete(any());
        
//         System.out.println("===== KẾT THÚC TEST: deleteQuiz_NotFound =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra xóa Quiz với lỗi file storage")
//     void deleteQuiz_FileStorageError() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: deleteQuiz_FileStorageError =====");
        
//         Long id = 1L;
//         String photoPath = "quiz/photo.jpg";
//         mockQuiz.setCoverPhoto(photoPath);
        
//         when(quizRepository.findById(id)).thenReturn(Optional.of(mockQuiz));
//         doThrow(new IOException("Lỗi xóa file")).when(fileStorageService).deleteFile(photoPath);
        
//         System.out.println("Đang gọi quizService.deleteQuiz với lỗi file storage");
//         IOException exception = assertThrows(IOException.class, () -> {
//             quizService.deleteQuiz(id);
//         });
        
//         System.out.println("Kiểm tra exception message");
//         assertEquals("Lỗi xóa file", exception.getMessage());
        
//         verify(quizRepository).findById(id);
//         verify(fileStorageService).deleteFile(photoPath);
//         verify(quizRepository, never()).delete(any());
        
//         System.out.println("===== KẾT THÚC TEST: deleteQuiz_FileStorageError =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra lấy Quiz theo Collection thành công")
//     void getQuizzesByCollection_Success() {
//         System.out.println("===== BẮT ĐẦU TEST: getQuizzesByCollection_Success =====");
        
//         Set<Quiz> expectedQuizzes = new HashSet<>(Arrays.asList(
//             TestDataFactory.createMockQuiz(1L),
//             TestDataFactory.createMockQuiz(2L)
//         ));
//         when(quizRepository.findByQuizCollection(mockQuizCollection)).thenReturn(expectedQuizzes);
        
//         System.out.println("Đang gọi quizService.getQuizzesByCollection");
//         Set<Quiz> result = quizService.getQuizzesByCollection(mockQuizCollection);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(2, result.size());
//         assertEquals(expectedQuizzes, result);
        
//         verify(quizRepository).findByQuizCollection(mockQuizCollection);
        
//         System.out.println("===== KẾT THÚC TEST: getQuizzesByCollection_Success =====");
//     }

//     @Test
//     @DisplayName("Kiểm tra lấy Quiz theo Collection ID thành công")
//     void getQuizzesByCollectionId_Success() {
//         System.out.println("===== BẮT ĐẦU TEST: getQuizzesByCollectionId_Success =====");
        
//         Long collectionId = 1L;
//         Set<Quiz> expectedQuizzes = new HashSet<>(Arrays.asList(
//             TestDataFactory.createMockQuiz(1L),
//             TestDataFactory.createMockQuiz(2L)
//         ));
//         when(quizRepository.findByQuizCollectionId(collectionId)).thenReturn(expectedQuizzes);
        
//         System.out.println("Đang gọi quizService.getQuizzesByCollectionId");
//         Set<Quiz> result = quizService.getQuizzesByCollectionId(collectionId);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(2, result.size());
//         assertEquals(expectedQuizzes, result);
        
//         verify(quizRepository).findByQuizCollectionId(collectionId);
        
//         System.out.println("===== KẾT THÚC TEST: getQuizzesByCollectionId_Success =====");
//     }

//  @Test
//     @DisplayName("Kiểm tra lấy Quiz theo Collection ID trả về rỗng")
//     void getQuizzesByCollectionId_EmptyResult() {
//         System.out.println("===== BẮT ĐẦU TEST: getQuizzesByCollectionId_EmptyResult =====");
        
//         Long collectionId = 999L;
//         when(quizRepository.findByQuizCollectionId(collectionId)).thenReturn(Collections.emptySet());
        
//         System.out.println("Đang gọi quizService.getQuizzesByCollectionId với ID không có Quiz");
//         Set<Quiz> result = quizService.getQuizzesByCollectionId(collectionId);
        
//         System.out.println("Kiểm tra kết quả trả về phải rỗng");
//         assertNotNull(result);
//         assertTrue(result.isEmpty());
        
//         verify(quizRepository).findByQuizCollectionId(collectionId);
        
//         System.out.println("===== KẾT THÚC TEST: getQuizzesByCollectionId_EmptyResult =====");
//     }

    
//     @Test
//     @DisplayName("Kiểm tra tạo Quiz với các trường bắt buộc")
//     void createQuiz_RequiredFields_Success() throws IOException {
//         System.out.println("===== BẮT ĐẦU TEST: createQuiz_RequiredFields_Success =====");
        
//         QuizDTO minimalDTO = QuizDTO.builder()
//                 .userId(1L)
//                 .title("Required Title")
//                 .description("Required Description")
//                 .keyword("required")
//                 .visible(true)
//                 .visibleQuizQuestion(true)
//                 .shuffle(false)
//                 .quizCollectionId("1")
//                 .build();
        
//         when(quizCollectionRepository.findById(1L)).thenReturn(Optional.of(mockQuizCollection));
//         when(quizRepository.save(any(Quiz.class))).thenReturn(mockQuiz);
        
//         System.out.println("Đang gọi quizService.createQuiz với các trường bắt buộc");
//         Quiz result = quizService.createQuiz(minimalDTO, null);
        
//         System.out.println("Kiểm tra kết quả trả về");
//         assertNotNull(result);
//         assertEquals(mockQuiz, result);
        
//         verify(quizRepository).save(any(Quiz.class));
        
//         System.out.println("===== KẾT THÚC TEST: createQuiz_RequiredFields_Success =====");
//     }

}