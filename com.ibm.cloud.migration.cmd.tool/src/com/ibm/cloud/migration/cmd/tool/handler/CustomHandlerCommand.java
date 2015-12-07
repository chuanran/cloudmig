package com.ibm.cloud.migration.cmd.tool.handler;

import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public abstract interface CustomHandlerCommand
{
  public abstract String getHandlerName();

  public abstract void handle(CommandLine paramCommandLine)
    throws IOException, Exception;
  public abstract String getDescription();
  
  public abstract String getOptionHelp();

  public abstract Options getOptions();
}
