/*
 * Copyright (c) 2021-2022 Bradley Larrick. All rights reserved.
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.larrick.maven.plugins.example;

import java.io.File;
import java.util.Arrays;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

/**
 * Formats HTML files in the given directory path.
 */
@SuppressWarnings("unused")
@Mojo(name = "example", defaultPhase = LifecyclePhase.SITE)
public class ExampleMojo extends AbstractMojo {

  /**
   * Directory of files to be formatted.
   */
  @Parameter(property = "example.source", defaultValue = "${project.build.directory}",
             required = true)
  protected File sourceDirectory;

  /**
   * Directory to write formatted files. If not specified, the formatted files over-write the source
   * files.
   */
  @Parameter(property = "example.output")
  protected File outputDirectory;

  /**
   * List of files to include. Specified as fileset patterns which are relative to the source
   * directory whose content is being formatted.
   */
  @Parameter(property = "example.includes", defaultValue = "**/*")
  protected String[] includes;

  /**
   * List of files to exclude. Specified as fileset patterns which are relative to the source
   * directory whose content is being formatted.
   */
  @Parameter(property = "example.excludes")
  protected String[] excludes;

  /**
   * Specifies whether the formatting should be skipped.
   */
  @Parameter(property = "example.skip", defaultValue = "false")
  protected boolean skip;

  /**
   * Specified verbose logging mode.
   */
  @Parameter(property = "example.verbose", defaultValue = "false")
  protected boolean verbose;

  /**
   * Default constructor. Initializes the output settings for jSoup.
   */
  public ExampleMojo() {
  }

  /**
   * Formats the files specified by the given parameters.
   *
   * @throws MojoFailureException if the update does not succeed
   */
  public void execute() throws MojoFailureException {

    if (!verbose) {
      verbose = getLog().isDebugEnabled();
    }

    if (verbose) {
      printConfiguration();
    }

    if (skip) {
      getLog().info("Skipping file formatting");
      return;
    }

    if (outputDirectory == null) {
      outputDirectory = sourceDirectory.getAbsoluteFile();
    }

    FileSetManager fileSetManager = new FileSetManager();
    FileSet        thisFileSet    = new FileSet();
    thisFileSet.setDirectory(sourceDirectory.getAbsolutePath());
    thisFileSet.setIncludes(Arrays.asList(includes));
    thisFileSet.setExcludes(Arrays.asList(excludes));

    String[] includedFiles = fileSetManager.getIncludedFiles(thisFileSet);

    int count = 0;
    for (String includedFile : includedFiles) {
      if (includedFile.endsWith("html")) {
        getLog().info("Found file " + includedFile);
        count++;
      }
    }

    getLog().info("Processed " + count + " files");
  }
  /**
   * Prints the Mojo configuration parameters.
   */
  void printConfiguration() {

    getLog().info("example-maven-plugin configuration:");
    getLog().info("sourceDirectory = " + sourceDirectory);
    getLog().info("outputDirectory = " + outputDirectory);
    getLog().info("includes:");
    for (String include : includes) {
      getLog().info("   " + include);
    }
    getLog().info("excludes:");
    for (String exclude : excludes) {
      getLog().info("   " + exclude);
    }
    getLog().info("skip = " + skip);
  }
}
