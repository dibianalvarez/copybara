    console = new TestingConsole()
        .respondNo();
    process(destinationFirstCommit(/*askConfirmation*/ true), new DummyReference("origin_ref"));
  }

  @Test
  public void processUserConfirms() throws Exception {
    console = new TestingConsole()
        .respondYes();
    yaml.setFetch("master");
    yaml.setPush("master");
    Files.write(workdir.resolve("test.txt"), "some content".getBytes());
    process(destinationFirstCommit(/*askConfirmation*/ true), new DummyReference("origin_ref"));
        .assertNextMatches(MessageType.PROGRESS, "Git Destination: Fetching file:.*")
        .assertNextMatches(MessageType.PROGRESS, "Git Destination: Adding files for push")
        .assertNextEquals(MessageType.INFO, "\n"
            + "diff --git a/test.txt b/test.txt\n"
            + "new file mode 100644\n"
            + "index 0000000..f0eec86\n"
            + "--- /dev/null\n"
            + "+++ b/test.txt\n"
            + "@@ -0,0 +1 @@\n"
            + "+some content\n"
            + "\\ No newline at end of file\n")
        .assertNextMatches(MessageType.WARNING, "Proceed with push to.*[?]")
        .assertNextMatches(MessageType.PROGRESS, "Git Destination: Pushing to .*")