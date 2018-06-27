/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import java.security.Permission;

/**
 *
 * @author behnish
 * ExitManager sm;
        sm = new ExitManager(System.getSecurityManager());
        System.setSecurityManager(sm);
 */
public class ExitManager extends SecurityManager {

    SecurityManager original;

    public ExitManager(SecurityManager original) {
        this.original = original;
    }

    /**
     * Deny permission to exit the VM.
     */
    @Override
    public void checkExit(int status) {
        throw (new SecurityException());
    }

    /**
     * Allow this security manager to be replaced, if fact, allow pretty much
     * everything.
     */
    @Override
    public void checkPermission(Permission perm) {
    }

    public SecurityManager getOriginalSecurityManager() {
        return original;
    }
}