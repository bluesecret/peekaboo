package io.wangk.peekaboo.common.pojo;

import io.wangk.peekaboo.common.utils.MapstructUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Schema(description = "分页结果")
@Data
public final class PageResult<T> implements Serializable {

    @Schema(description = "数据", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<T> list;

    @Schema(description = "总量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long total;

    public PageResult() {
    }

    public PageResult(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }

    public PageResult(Long total) {
        this.list = new ArrayList<>();
        this.total = total;
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(0L);
    }

    public static <T> PageResult<T> empty(Long total) {
        return new PageResult<>(total);
    }

    public void forEach(Consumer<T> action) {
        if (this.list != null) {
            this.list.forEach(action);
        }
    }

    public <R> PageResult<R> map(Function<? super T, ? extends R> mapper) {
        PageResult<R> r = new PageResult<>();
        r.setTotal(this.total);
        if (this.list != null) {
            r.setList(this.list.stream().map(mapper).collect(Collectors.toList()));
        }
        return r;
    }

    public <R> PageResult<R> map(Class<R> target) {
        PageResult<R> r = new PageResult<>();
        r.setTotal(this.total);
        r.setList(MapstructUtils.convert(this.list, target));
        return r;
    }
}
