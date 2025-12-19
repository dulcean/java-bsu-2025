package waifu.repository;

import waifu.model.Waifu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WaifuRepository extends JpaRepository<Waifu, Long> {
    Optional<Waifu> findFirstByWaifuNameAndImageUrlIsNotNullOrderByIdDesc(String waifuName);

    @Query(value = "SELECT waifu_name as waifuName, COUNT(*) as count FROM waifus GROUP BY waifu_name ORDER BY count DESC", nativeQuery = true)
    List<WaifuStats> getGlobalStats();
}