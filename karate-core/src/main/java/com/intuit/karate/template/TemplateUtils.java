/*
 * The MIT License
 *
 * Copyright 2020 Intuit Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.intuit.karate.template;

import com.intuit.karate.graal.JsEngine;
import com.intuit.karate.http.ServerConfig;
import com.intuit.karate.resource.ResourceResolver;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;

/**
 *
 * @author pthomas3
 */
public class TemplateUtils {

    private TemplateUtils() {
        // only static methods
    }

    private static final String HTMX_SCRIPT_TAG = "<script src=\"https://unpkg.com/htmx.org@1.2.0\"></script>";

    public static IModel generateHeadScriptTag(ITemplateContext ctx) {
        IModelFactory modelFactory = ctx.getModelFactory();
        return modelFactory.parse(ctx.getTemplateData(), HTMX_SCRIPT_TAG);
    }

    public static boolean hasAncestorElement(ITemplateContext ctx, String name) {
        for (IProcessableElementTag tag : ctx.getElementStack()) {
            if (tag.getElementCompleteName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static KarateTemplateEngine forServer(ServerConfig config) {
        KarateTemplateEngine engine = new KarateTemplateEngine(null, new KarateServerDialect(config));
        engine.setTemplateResolver(new ServerHtmlTemplateResolver(config.getResourceResolver()));
        return engine;
    }

    public static KarateTemplateEngine forStrings(JsEngine je) {
        KarateTemplateEngine engine = new KarateTemplateEngine(je);
        engine.setTemplateResolver(StringHtmlTemplateResolver.INSTANCE);
        return engine;
    }

    public static KarateTemplateEngine forResourcePath(JsEngine je, String root) {
        ResourceResolver resourceResolver = new ResourceResolver(root);
        KarateTemplateEngine engine = new KarateTemplateEngine(je, new KarateScriptDialect(resourceResolver));
        engine.setTemplateResolver(new ResourceHtmlTemplateResolver(resourceResolver));
        return engine;
    }

}
