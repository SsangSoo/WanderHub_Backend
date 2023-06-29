package wanderhub.server.global.utils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;

@Component
public class CustomBeanUtils<T> {

    public T copyNonNullProoerties(T source, T destination) { // source 원래 DB에 저장되있던 정보, destination이 Controller로부터 받아온 정보
        // 예외가 발생하는 조건
        // source와 destination이 하나라도 null이거나 / 클래스 타입이 다르면 예외
        if(source == null || destination == null || source.getClass() != destination.getClass()) {
            return null;
        }

        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper dest = new BeanWrapperImpl(destination);
        // sourc와 destination을 래핑한다. 래핑된 객체들을 통해서 값을 할당할 수 있다.


        for(final Field property : source.getClass().getDeclaredFields()) {     // source의 모든 필드들을 가져온다.
            Object sourceProperty = src.getPropertyValue(property.getName());   // 프로퍼티(=속성,필드)의 이름에 따른 값들을 Object 타입의 sourceProperty에 넣는다.

            if(sourceProperty != null && !(sourceProperty instanceof Collection<?>)) { // sourceProperty가 null이 아니거나, Collection타입이 아니라면
                dest.setPropertyValue(property.getName(), sourceProperty);      // dest객체의 해당필드에 src로부터 얻어온 property값을 할당한다.
            }
        }

        return destination;
    }
}
