package com.tenco.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository // IoC
@RequiredArgsConstructor // DI 처리 됨
public class BoardPersistRepository {

    // JPA 핵심 인터페이스
    // 영속성 컨텍스트를 관리하고 엔티티의 생명주기를 제어
    // @Autowired // DI
    private final EntityManager em; // final 사용하면 성능 개선이 조금 됨

    // 의존 주입 (외부에서 생성되어 있는 객체의 주소값을 주입 받다)
//    public BoardPersistRepository(EntityManager em) {
//        this.em = em;
//    }

    // 게시글 저장
    @Transactional
    public Board save(Board board) {
        em.persist(board); // insert 처리 완료
        return board;
    }

    // JPQL을 사용한 게시글 목록 조회
    public List<Board> findAll() {
        // JOIN FETCH 사용 쿼리 변경 함
        // N + 1 문제를 해결하는 정밀 제어 , JOIN FETCH 은 그냥 한번에 다 가져와
        String jpqlStr = "SELECT b FROM Board b JOIN FETCH b.user ORDER BY b.id DESC";
        List<Board> boardList = em.createQuery(jpqlStr, Board.class).getResultList();

        return boardList;
    }


    // 게시글 상세보기 요청 (조회) (필수값 기본키로 조회)
    public Board findById(Integer id) {

        // 영속성 컨텍스트를 사용하기 위해
        // 1. 엔티티 매니저에서 제공하는 메서드를 활용 방법
        Board board = em.find(Board.class, id);

        // 2. JPQL 문법으로 Board 를 조회 하는 방법
//        String jpql = """
//                SELECT b FROM Board b WHERE b.id = :id
//                """;

//        return em.createQuery(jpql, Board.class)
//                .setParameter("id", id)
//                .getSingleResult();
        return board;
    }

    // 게시글 삭제
    @Transactional
    public void deleteById(Integer id) {
        // 1. 먼저 삭제 하고자하는 엔티티를 조회
        // 1.1 조회가 되었기 때문에 board 는 영속화 된 상태가 되었다.
        Board board = em.find(Board.class, id);
        if (board == null) {
            throw new IllegalArgumentException("삭제할 게시글을 찾을 수 없습니다 : " + id);
        }
        em.remove(board);
    }

    @Transactional
    public Board updateById(Integer id, BoardRequest.UpdateDTO updateDTO) {

        Board boardEntity = em.find(Board.class, id);
        if(boardEntity == null) {
            throw new IllegalArgumentException("수정할 게시글을 찾을 수 없습니다 : " + id);
        }
        boardEntity.update(updateDTO);
        return boardEntity;
    }
}





