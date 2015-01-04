/*
 * $Id: BugzillaTaskBuilder.java 14 2014-12-04 06:51:44Z jierong $
 * 
 * Copyright (c) 2001-2008 Accentrix Company Limited. All Rights Reserved.
 * 
 * Accentrix Company Limited. ("Accentrix") retains copyright on all text, source
 * and binary code contained in this software and documentation. Accentrix grants
 * Licensee a limited license to use this software, provided that this copyright
 * notice and license appear on all copies of the software. The software source
 * code is provided for reference purposes only and may not be copied, modified 
 * or distributed.
 * 
 * THIS SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS," WITHOUT ANY WARRANTY OF
 * ANY KIND UNLESS A SEPARATE WARRANTIES IS PURCHASED FROM ACCENTRIX AND REMAINS
 * VALID.  ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. ACCENTRIX SHALL NOT BE LIABLE
 * FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING OR MODIFYING THE
 * SOFTWARE OR ITS DERIVATIVES.
 * 
 * IN NO EVENT WILL ACCENTRIX BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE
 * USE OF OR INABILITY TO USE SOFTWARE, EVEN IF ACCENTRIX HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 */
package com.accentrix.ci.bugzillascrum;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

public class BugzillaTaskBuilder extends Builder {

    private final String productName;
    private final String sendTos;

    // Fields in config.jelly must match the parameter names in the
    // "DataBoundConstructor"
    @DataBoundConstructor
    public BugzillaTaskBuilder(String productName, String sendTos) {
        this.productName = productName;
        this.sendTos = sendTos;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getProductName() {
        return productName;
    }

    public String getSendTos() {
        return sendTos;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {

        runGroovy(listener);

        return true;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    public void runGroovy(BuildListener listener) {
        String path = BugzillaTaskBuilder.class.getProtectionDomain().getCodeSource().getLocation()
                .getPath();

        GroovyScriptEngine gse = null;
        String groovyFile = "deployNotification.groovy";
        String cssFile = "report.css";

        try {
            if (path.endsWith(".jar")) {
                JarFile jarFile = new JarFile(new File(path));

                path = path.substring(0, path.lastIndexOf(File.separator));

                JarEntry groovyEntry = jarFile.getJarEntry(groovyFile);
                InputStream inputStream = jarFile.getInputStream(groovyEntry);
                copyTo(inputStream, new File(path + File.separator + groovyFile));

                JarEntry cssEntry = jarFile.getJarEntry(cssFile);
                inputStream = jarFile.getInputStream(cssEntry);
                copyTo(inputStream, new File(path + File.separator + cssFile));
            }

            listener.getLogger().println("path: " + path);
            gse = new GroovyScriptEngine(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Binding binding = new Binding();
            binding.setVariable("productName", productName);
            binding.setVariable("sendTos", sendTos);
            binding.setVariable("cssFile", path + File.separator + cssFile);
            gse.run(groovyFile, binding);
        } catch (ResourceException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (ScriptException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void copyTo(InputStream in, File toFile) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(toFile);
            copyStream(in, out);
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        final int MAX = 4096;
        byte[] buf = new byte[MAX];
        for (int bytesRead = in.read(buf, 0, MAX); bytesRead != -1; bytesRead = in
                .read(buf, 0, MAX)) {
            out.write(buf, 0, bytesRead);
        }
    }

    /**
     * Descriptor for {@link HelloWorldBuilder}. Used as a singleton. The class
     * is marked as public so that it can be accessed from views.
     *
     * <p>
     * See
     * <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension
    // This indicates to Jenkins that this is an implementation of an extension
    // point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * To persist global configuration information, simply store it in a
         * field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */
        private boolean useFrench;

        /**
         * In order to load the persisted global configuration, you have to call
         * load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *            This parameter receives the value that the user has typed.
         * @return Indicates the outcome of the validation. This is sent to the
         *         browser.
         *         <p>
         *         Note that returning {@link FormValidation#error(String)} does
         *         not prevent the form from being saved. It just means that a
         *         message will be displayed to the user.
         */
        public FormValidation doCheckName(@QueryParameter String value) throws IOException,
                ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set a name");
            if (value.length() < 4)
                return FormValidation.warning("Isn't the name too short?");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project
            // types
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Bugzilla Task Board";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            useFrench = formData.getBoolean("useFrench");
            // ^Can also use req.bindJSON(this, formData);
            // (easier when there are many fields; need set* methods for this,
            // like setUseFrench)
            save();
            return super.configure(req, formData);
        }

        /**
         * This method returns true if the global configuration says we should
         * speak French.
         *
         * The method name is bit awkward because global.jelly calls this method
         * to determine the initial state of the checkbox by the naming
         * convention.
         */
        public boolean getUseFrench() {
            return useFrench;
        }
    }
}
