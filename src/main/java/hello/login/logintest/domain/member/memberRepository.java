package hello.login.logintest.domain.member;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class memberRepository {

    private static Map<Long, Member> store = new ConcurrentHashMap<>();
    private static long sequence =0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId){
        return this.findAll().stream()
                .filter(m-> m.getUserId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
