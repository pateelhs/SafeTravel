// Generated code from Butter Knife. Do not modify!
package com.agiledge.keocometemployee;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class PanicAdminActivity$$ViewBinder<T extends PanicAdminActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(Finder finder, T target, Object source) {
    return new InnerUnbinder<>(target, finder, source);
  }

  protected static class InnerUnbinder<T extends PanicAdminActivity> implements Unbinder {
    protected T target;

    private View view2131624321;

    private View view2131624322;

    protected InnerUnbinder(final T target, Finder finder, Object source) {
      this.target = target;

      View view;
      target.droppanic = finder.findRequiredViewAsType(source, 2131624124, "field 'droppanic'", Spinner.class);
      target.inputComment1 = finder.findRequiredViewAsType(source, 2131624125, "field 'inputComment1'", EditText.class);
      view = finder.findRequiredView(source, 2131624321, "method 'onApproveClick'");
      view2131624321 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.onApproveClick();
        }
      });
      view = finder.findRequiredView(source, 2131624322, "method 'onResetClick'");
      view2131624322 = view;
      view.setOnClickListener(new DebouncingOnClickListener() {
        @Override
        public void doClick(View p0) {
          target.onResetClick();
        }
      });
    }

    @Override
    public void unbind() {
      T target = this.target;
      if (target == null) throw new IllegalStateException("Bindings already cleared.");

      target.droppanic = null;
      target.inputComment1 = null;

      view2131624321.setOnClickListener(null);
      view2131624321 = null;
      view2131624322.setOnClickListener(null);
      view2131624322 = null;

      this.target = null;
    }
  }
}
