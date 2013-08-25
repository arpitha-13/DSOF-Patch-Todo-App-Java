/*
 * The MIT License
 *
 *   Copyright (c) 2013, benas (md.benhassine@gmail.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package net.benas.todolist.web.struts.action.user;

import com.opensymphony.xwork2.Action;
import net.benas.todolist.core.domain.User;
import net.benas.todolist.web.common.form.ChangePasswordForm;
import net.benas.todolist.web.common.form.RegistrationForm;
import net.benas.todolist.web.common.util.TodolistUtils;
import net.benas.todolist.web.struts.action.BaseAction;

/**
 * Action class for Account CRUD operations.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class AccountAction extends BaseAction {

    private ChangePasswordForm changePasswordForm;

    private RegistrationForm registrationForm;

    private User user;

    private String updateProfileSuccessMessage, updatePasswordSuccessMessage;

    public String account() {
        user = getSessionUser();
        return Action.SUCCESS;
    }

    public String register() {
        return Action.SUCCESS;
    }

    public String doRegister() {
        // Todo registration form validation
        User user = new User(registrationForm.getFirstname(), registrationForm.getLastname(), registrationForm.getEmail(), registrationForm.getPassword());
        user = userService.create(user);
        session.put(TodolistUtils.SESSION_USER, user);
        return Action.SUCCESS;
    }

    public String doUpdate() {
        //Todo validate profile update form (check existing email)
        User user = getSessionUser();
        user.setFirstname(this.user.getFirstname());
        user.setLastname(this.user.getLastname());
        user.setEmail(this.user.getEmail());
        userService.update(user);
        session.put(TodolistUtils.SESSION_USER, user);
        updateProfileSuccessMessage = getText("account.profile.update.success");
        return Action.SUCCESS;
    }

    public String doDelete() {
        User user = getSessionUser();
        //remove user
        userService.remove(user);

        //invalidate session
        session.put(TodolistUtils.SESSION_USER, null);
        if (session instanceof org.apache.struts2.dispatcher.SessionMap) {
            try {
                ((org.apache.struts2.dispatcher.SessionMap) session).invalidate();
            } catch (IllegalStateException e) {
                logger.error("Unable to invalidate session.", e);
            }
        }
        return Action.SUCCESS;
    }

    public String doChangePassword() {
        //Todo validate password change form

        User user = getSessionUser();
        if (!changePasswordForm.getCurrentpassword().equals(user.getPassword())) {
            //Todo set errors
            return Action.INPUT;
        }

        if (!changePasswordForm.getPassword().equals(changePasswordForm.getConfirmpassword())) {
            //Todo set errors
            return Action.INPUT;
        }

        user.setPassword(changePasswordForm.getPassword());
        user = userService.update(user);
        session.put(TodolistUtils.SESSION_USER, user);
        this.user = user;
        updatePasswordSuccessMessage = getText("account.password.update.success");
        return Action.SUCCESS;
    }

    /*
     * Getters for model attributes
     */
    public String getRegisterTabStyle() {
        return "active";
    }

    public ChangePasswordForm getChangePasswordForm() {
        return changePasswordForm;
    }

    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public User getUser() {
        return user;
    }

    public String getUpdateProfileSuccessMessage() {
        return updateProfileSuccessMessage;
    }

    public String getUpdatePasswordSuccessMessage() {
        return updatePasswordSuccessMessage;
    }

    /*
     * Setters for request parameters binding
     */

    public void setChangePasswordForm(ChangePasswordForm changePasswordForm) {
        this.changePasswordForm = changePasswordForm;
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    public void setUser(User user) {
        this.user = user;
    }

}