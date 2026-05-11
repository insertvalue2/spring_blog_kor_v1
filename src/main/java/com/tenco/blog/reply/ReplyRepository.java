package com.tenco.blog.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// @Repository - 부모의 클래스에 정의 되어 있음()
public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    // 기본적인 CRUD 자동 완성 및 추가 편의 기능 자동 생성


//    select r.*, b.*, u.*
//    from reply_tb r
//    inner join board_tb b on r.board_id = b.id
//    inner join user_tb u on r.user_id = u.id
//    where r.board_id = 1
//    order by r.created_at asc;
    // JPQL 문법으로 변환
    // 게시글 ID로 댓글 목록 조회(한번에 댓글 작성자 정보 포함 - JOIN FETCH 사용)
    @Query("""
    SELECT r FROM Reply r 
    JOIN FETCH r.user 
    JOIN FETCH r.board 
    WHERE r.board.id = :boardId 
    ORDER BY r.createdAt ASC
""")
    List<Reply> findByBoardIdWithUser(@Param("boardId") Integer boardId);

}
