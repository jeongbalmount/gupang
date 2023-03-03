package shoppingMall.gupang.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter
@NoArgsConstructor(access = PROTECTED)
public class Review {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "likes")
    // 좋아요 버튼 눌린 횟수
    private int like = 0;

    private String title;

    @Column(length = 2000)
    private String content;

    private LocalDateTime writeDate;

    public Review(Member member, Item item, String title, String content) {
        this.member = member;
        this.item = item;
        this.title = title;
        this.content = content;
        this.writeDate = LocalDateTime.now();
    }

    public void addLike() {
        this.like += 1;
    }

    public void changeTitle(String newTitle) {
        this.title = newTitle;
    }

    public void changeContents(String newContent) {
        this.content = newContent;
    }

    public void setLike(int likes) {
        like = likes;
    }

}
