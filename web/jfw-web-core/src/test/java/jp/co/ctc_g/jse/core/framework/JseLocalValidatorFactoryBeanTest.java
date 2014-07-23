package jp.co.ctc_g.jse.core.framework;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.validation.metadata.ConstraintDescriptor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.DefaultMessageSourceResolvable;

@RunWith(MockitoJUnitRunner.class)
public class JseLocalValidatorFactoryBeanTest {
    
    @Mock
    private ConstraintDescriptor<?> descriptor;
    
    private JseLocalValidatorFactoryBean validator;
    
    @Before
    public void setup() {
        validator = new JseLocalValidatorFactoryBean();
    }
    
    @Test
    public void 意図したサフィックスが付与されたArgumentsが生成される() {
        Object[] args = validator.getArgumentsForConstraint("profile", "name", descriptor);
        assertThat(args, is(notNullValue()));
        assertThat(args.length, is(1));
        assertThat(args[0], is(instanceOf(DefaultMessageSourceResolvable.class)));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes().length, is(4));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[0], is("profile.name"));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[1], is("profileCriteria.name"));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[2], is("profileSelection.name"));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[3], is("name"));
        
        args = validator.getArgumentsForConstraint("profileCriteria", "name", descriptor);
        assertThat(args, is(notNullValue()));
        assertThat(args.length, is(1));
        assertThat(args[0], is(instanceOf(DefaultMessageSourceResolvable.class)));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes().length, is(4));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[0], is("profileCriteria.name"));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[1], is("profile.name"));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[2], is("profileSelection.name"));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[3], is("name"));
        
        args = validator.getArgumentsForConstraint("profileSelection", "name", descriptor);
        assertThat(args, is(notNullValue()));
        assertThat(args.length, is(1));
        assertThat(args[0], is(instanceOf(DefaultMessageSourceResolvable.class)));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes().length, is(4));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[0], is("profileSelection.name"));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[1], is("profile.name"));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[2], is("profileCriteria.name"));
        assertThat(((DefaultMessageSourceResolvable)args[0]).getCodes()[3], is("name"));
        
    }

}
