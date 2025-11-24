
Test Plan (short)
1. Authentication
 - Wrong password shows error.
 - Correct credentials open role dashboard.
2. Student flow
 - Catalog shows sections.
 - Register in a section with free seats -> success.
 - Register in full section -> blocked.
 - Duplicate register -> blocked.
 - Drop before deadline -> success.
 - Transcript export exists.
3. Instructor flow
 - See only assigned sections.
 - Enter scores saved.
 - Compute final grade shows expected result.
4. Admin flow
 - Create course/section and assign instructor.
 - Toggle maintenance blocks writes.
5. Data integrity
 - Duplicate enrollments prevented by DB unique constraint.
